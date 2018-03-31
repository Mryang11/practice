VolinPlot <-
function(mutdata,output)
{
	data=as.matrix(read.table(mutdata,sep="\t"))
	a=data[3:dim(data)[1],2:dim(data)[2]]
	mutation=data[,1]
	colname1=data[1,3:dim(data)[2]]
	colname2=data[2,3:dim(data)[2]]	
	if(dim(data)[1]==3){a=t(matrix(a))}
	if(dim(a)[1]==1&sum(as.numeric(a))==0)
	{
		png(file=output,res=92,width=800,height=600,family="MSungStd-Light-Acro",type="cairo")
		plot(2,2,xlim=c(0,(dim(a)[2]-1+0.05)),ylim=c(-2.5/4,2.5/4),type="n",bty="n",axes=F,xlab="",ylab="")
		lines(c(0,(dim(a)[2]-1)),c(0,0))
		for(i in 1:(dim(a)[2]-1))
		{
			lines(c(i,i),c(-(2.2/4),2.2/4),lty=2)
			x=colname2[3:dim(data)[2]]
			text(i,-(2.3/4),colname1[i],cex=1,family="times new roman")
			text(i,-(2.43/4),colname2[i],cex=1,family="times new roman")
		}
		dev.off()
	}
	else
	{
		b=c(0:(dim(a)[2]-1))
		t=matrix(0,1,dim(a)[2])
		for(i in 1:dim(a)[2])
		{
			t[1,i]=sum(as.numeric(a[,i]))
		}
		# png(file=output,width=800,height=600,family="MSungStd-Light-Acro",type="cairo")
		png(file=output,res=92,width=800,height=600,family="MSungStd-Light-Acro",type="cairo")
		plot(2,2,xlim=c(0,(dim(a)[2]-1+0.05)),ylim=c(-(max(t)*2.5/4),max(t)*2.5/4),type="n",bty="n",axes=F,xlab="",ylab="")
		tem=t/2
		res=t/2
		for(i in 1:dim(a)[1])
		{
			nextpos1=tem-as.numeric(a[i,])
			tem=nextpos1
			res=rbind(res,nextpos1)
		}
		range=max(b)-min(b)
		flank=range*0.001
		time=c(rbind(b-flank*2,b-flank,b,b+flank,b+flank*2))
		af1=NULL
		borderaf=NULL
		for(i in 1:dim(res)[2])
		{
			af1=cbind(af1,matrix(res[,i],dim(res)[1],5))
			borderaf=cbind(borderaf,matrix(t[i],1,5))
		}
		times=spline(as.numeric(time),as.numeric(borderaf/2),n=100)$x
		AF=NULL
		for(i in 1:dim(af1)[1])
		{
			atm=spline(as.numeric(time),as.numeric(af1[i,]),n=100)$y
			AF=rbind(AF,atm)
		}
		#color=c("DodgerBlue","DeepSkyBlue","SpringGreen3","IndianRed2","Goldenrod1","DarkOrange","Violet","Firebrick2","Tomato2","HotPink2")
		color=c("yellowgreen","springgreen3","chartreuse3","deepskyblue2","dodgerblue2","steelblue2","IndianRed2","palevioletred2","sienna1","lightcoral")
		
		for(i in 1:(dim(AF)[1]-1))
		{
			polygon(x =c(times,rev(times)) ,y = c(AF[i,],rev(AF[i+1,])),col=color[i],border = NA)
		}
		for(i in 1:(dim(a)[2]-1))
		{
			#abline(v=i,lty=2)
			lines(c(i,i),c(-(max(t)*2.2/4),max(t)*2.2/4),lty=2)
			x=colname2[3:dim(data)[2]]
			text(i,-(max(t)*2.3/4),colname1[i],cex=1,family="times new roman")
			text(i,-(max(t)*2.43/4),colname2[i],cex=1,family="times new roman")
		}
		tisx=0.005*length(b)
		tisy=0.02*length(b)
		start=min(time)+tisx
		end=max(time)
		# LINE WRAP GAP
		tisyg=0
		mutation=mutation[3:length(mutation)]
		for(i in 1:dim(a)[1])
		{
			if((start+0.05*length(b))>=length(b))
			{
				tisy=tisy+0.15
				start=min(time)+tisx
			}
			polygon(x=c(start-tisx,start+tisx,start+tisx,start-tisx),y=c(-(max(t)*(2.635-tisy+tisyg)/4),-(max(t)*(2.635-tisy+tisyg)/4),-(max(t)*(2.6+tisy+tisyg)/4),-(max(t)*(2.6+tisy+tisyg)/4)),col=color[i],border = NA)
			text(start+tisx+0.01*length(b),-(max(t)*(2.635+tisyg)/4),mutation[i],cex=0.7,adj=c(0,0.5),family="times new roman") # 0.06 GAP 1
			start=start+0.1*length(b) # GAP 2
		}
		dev.off()
	}
	# return true
	return (1 == 1)
}
